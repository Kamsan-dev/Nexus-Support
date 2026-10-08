import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class PkceService {
  async generateCodeVerifier(): Promise<string> {
    const randomBytes = crypto.getRandomValues(new Uint8Array(32));
    return this.base64UrlEncode(randomBytes);
  }

  async generateCodeChallenge(codeVerifier: string): Promise<string> {
    const data = new TextEncoder().encode(codeVerifier);
    const digest = await crypto.subtle.digest('SHA-256', data);
    return this.base64UrlEncode(new Uint8Array(digest));
  }

  generateState(): string {
    const randomBytes = crypto.getRandomValues(new Uint8Array(32));
    return this.base64UrlEncode(randomBytes);
  }

  private base64UrlEncode(bytes: Uint8Array): string {
    const binary = String.fromCharCode(...bytes);
    return btoa(binary).replace(/\+/g, '-').replace(/\//g, '_').replace(/=/g, '');
  }
}
