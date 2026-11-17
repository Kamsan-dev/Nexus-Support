import { Injectable, inject } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Key } from '../enum/cache.key';
import { StorageService } from '../service/storage.service';
import { OAuthService } from './oauth.service';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private oauth = inject(OAuthService);
  private jwt = new JwtHelperService();
  private storage = inject(StorageService);

  private readonly server: string = environment.API_URL;
  readonly logoutUrl = `${this.server}/logout`;

  async login(): Promise<void> {
    await this.oauth.login();
  }
  logout(): void {
    this.oauth.logout();
  }

  async handleCallback(code: string, state: string): Promise<void> {
    const tokenResponse = await this.oauth.handleCallback(code, state);
    this.storage.set(Key.TOKEN, tokenResponse.access_token);
    this.storage.set(Key.REFRESH_TOKEN, tokenResponse.refresh_token);
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
