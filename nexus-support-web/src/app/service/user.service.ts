import { inject, Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { StorageService } from './storage.service';
import { Key } from '../enum/cache.key';
import { HttpClient } from '@angular/common/http';
import { CreateUserRequest } from '../core/request/create-user.request';
import { Observable } from 'rxjs';
import { ApiResponse } from '../core/response/api.response';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private jwt = new JwtHelperService();
  private storage = inject(StorageService);
  private http = inject(HttpClient);
  private readonly server: string = environment.API_URL;

  public register(request: CreateUserRequest): Observable<ApiResponse<null>> {
    return this.http.post<ApiResponse<null>>(`${this.server}user/register`, request);
  }
  public isAuthenticated(): boolean {
    const token = this.storage.get(Key.TOKEN);
    if (token == null) return false;
    if (this.jwt.decodeToken<string>(token) != null && !this.isTokenExpired(token)) return true;
    else return false;
  }

  private isTokenExpired(token: string): boolean {
    return this.jwt.isTokenExpired(token) ? true : false;
  }
}
