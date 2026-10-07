import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { delay, firstValueFrom } from 'rxjs';
import { PkceService } from './pcke.service';
import { OAuthStorageService } from './oauthstorage.service';
import { environment } from '../../environments/environment';

interface TokenResponse {
  access_token: string;
  refresh_token: string;
  token_type: string;
  expires_in: number;
  scope: string;
  id_token: string;
}

@Injectable({
  providedIn: 'root',
})
export class OAuthService {
  private readonly http = inject(HttpClient);
  private readonly pkce = inject(PkceService);
  private readonly storage = inject(OAuthStorageService);
  private readonly server: string = environment.API_URL;

  private readonly authorizationEndpoint = `${this.server}/oauth2/authorize`;

  private readonly tokenEndpoint = `${this.server}/oauth2/token`;

  private readonly clientId = 'client';

  private readonly redirectUri = 'http://localhost:3000/auth/callback';

  async login(): Promise<void> {
    const codeVerifier = await this.pkce.generateCodeVerifier();

    const codeChallenge = await this.pkce.generateCodeChallenge(codeVerifier);

    const state = this.pkce.generateState();

    this.storage.setCodeVerifier(codeVerifier);
    this.storage.setState(state);

    const params = new URLSearchParams({
      response_type: 'code',
      client_id: this.clientId,
      scope: 'openid',
      redirect_uri: this.redirectUri,
      code_challenge_method: 'S256',
      code_challenge: codeChallenge,
      state,
    });

    window.location.href = `${this.authorizationEndpoint}?${params.toString()}`;
  }

  async handleCallback(code: string, state: string): Promise<TokenResponse> {
    const storedState = this.storage.getState();

    const codeVerifier = this.storage.getCodeVerifier();

    if (!storedState || state !== storedState) {
      this.storage.clear();

      throw new Error('Invalid OAuth state');
    }

    if (!codeVerifier) {
      this.storage.clear();

      throw new Error('PKCE code verifier not found');
    }

    const body = new HttpParams()
      .set('grant_type', 'authorization_code')
      .set('client_id', this.clientId)
      .set('code', code)
      .set('redirect_uri', this.redirectUri)
      .set('code_verifier', codeVerifier);

    try {
      delay(5000);
      const response = await firstValueFrom(
        this.http.post<TokenResponse>(this.tokenEndpoint, body.toString(), {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
        }),
      );
      return response;
    } catch (error) {
      throw error;
    } finally {
      this.storage.clear();
    }
  }
}
