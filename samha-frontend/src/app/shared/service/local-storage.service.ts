import {Injectable} from '@angular/core';

@Injectable()
export class LocalStorageService {
  private readonly storage: Storage;

  constructor() {
    this.storage = window.localStorage;
  }

  set(key: string, value: any) {
    if (this.storage) {
      this.storage.setItem(key, JSON.stringify(value));
    }
  }

  get(key: string) {
    if (this.storage.getItem(key)) {
      return JSON.parse(this.storage.getItem(key));
    }
  }

  remove(key: string): boolean {
    if (this.storage) {
      this.storage.removeItem(key);
      return true;
    }
    return false;
  }

  clearTokens(): boolean {
    if (this.storage) {
      this.storage.removeItem('access_token');
      this.storage.removeItem('refresh_token');
      return true;
    }
    return false;
  }
}
