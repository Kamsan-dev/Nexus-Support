import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class OAuthStorageService {
  private readonly CODE_VERIFIER_KEY = 'oauth_code_verifier';
  private readonly STATE_KEY = 'oauth_state';

  setCodeVerifier(value: string): void {
    sessionStorage.setItem(this.CODE_VERIFIER_KEY, value);
  }

  getCodeVerifier(): string | null {
    return sessionStorage.getItem(this.CODE_VERIFIER_KEY);
  }

  setState(value: string): void {
    sessionStorage.setItem(this.STATE_KEY, value);
  }

  getState(): string | null {
    return sessionStorage.getItem(this.STATE_KEY);
  }

  clear(): void {
    sessionStorage.removeItem(this.CODE_VERIFIER_KEY);
    sessionStorage.removeItem(this.STATE_KEY);
  }
}
