import { inject, Injectable, InjectionToken } from '@angular/core';
import { Key } from '../enum/cache.key';

export const CLIENT_STORAGE = new InjectionToken<Storage>('CLIENT_STORAGE', {
  factory: () => localStorage,
});

@Injectable({ providedIn: 'root' })
export class StorageService {
  private readonly storage = inject(CLIENT_STORAGE);

  get(key: string): string | null {
    return this.storage.getItem(key);
  }

  set(key: string, value: unknown): void {
    this.storage.setItem(key, typeof value === 'string' ? value : JSON.stringify(value));
  }

  remove(key: string): void {
    this.storage.removeItem(key);
  }

  getRedirectUrl(): string | null {
    return this.get(Key.REDIRECT_URL);
  }

  setRedirectUrl(url: string): void {
    this.set(Key.REDIRECT_URL, url);
  }

  removeRedirectUrl(): void {
    this.storage.removeItem(Key.REDIRECT_URL);
  }
}
