import { Injectable, computed, inject, signal } from '@angular/core';
import { AuthSession } from '../core/model/auth-session.model';
import { OAuthService } from './oauth.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { StorageService } from './storage.service';
import { Key } from '../enum/cache.key';

interface TokenResponse {
  access_token: string;
  refresh_token?: string;
  token_type: string;
  expires_in: number;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private oauth = inject(OAuthService);
  private jwt = new JwtHelperService();
  private storage = inject(StorageService);

  private readonly session = signal<AuthSession | null>(null);

  readonly accessToken = computed(() => this.session()?.accessToken ?? null);

  // public isAuthenticated = computed(() => this.session() != null);

  async login(): Promise<void> {
    await this.oauth.login();
  }

  async handleCallback(code: string, state: string): Promise<void> {
    const tokenResponse = await this.oauth.handleCallback(code, state);
    this.storage.set(Key.TOKEN, tokenResponse.access_token);
    this.storage.set(Key.REFRESH_TOKEN, tokenResponse.refresh_token);
    this.session.set({
      accessToken: tokenResponse.access_token,
      refreshToken: tokenResponse.refresh_token,
      expiresAt: Date.now() + tokenResponse.expires_in * 1000,
    });
  }

  public isAuthenticated(): boolean {
    const token = this.storage.get(Key.TOKEN);
    if (token == null) return false;
    if (this.jwt.decodeToken<string>(token) != null && !this.isTokenExpired(token)) return true;
    else return false;
  }

  public isTokenExpired(token: string): boolean {
    return this.jwt.isTokenExpired(token) ? true : false;
  }
}
