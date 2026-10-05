import { inject, Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { StorageService } from './storage.service';
import { Key } from '../enum/cache.key';
import { HttpClient } from '@angular/common/http';
import { CreateUserRequest } from '../core/request/create-user.request';
import { Observable } from 'rxjs';
import { ApiResponse } from '../core/response/api.response';
import { environment } from '../../environments/environment';
import { DoResetPasswordRequest } from '../core/request/change-password.request';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private jwt = new JwtHelperService();
  private storage = inject(StorageService);
  private http = inject(HttpClient);
  private readonly server: string = environment.API_URL;

  public register(request: CreateUserRequest): Observable<ApiResponse<null>> {
    return this.http.post<ApiResponse<null>>(`${this.server}/user/register`, request);
  }

  public verifyAccount(token: string): Observable<ApiResponse<null>> {
    return this.http.post<ApiResponse<null>>(
      `${this.server}/user/verify/account?token=${token}`,
      null,
    );
  }

  //#region password management

  public resetPassword(formData: FormData): Observable<ApiResponse<null>> {
    return this.http.post<ApiResponse<null>>(`${this.server}/user/reset/password`, formData);
  }

  public verifyResetPasswordToken(token: string): Observable<ApiResponse<null>> {
    return this.http.get<ApiResponse<null>>(
      `${this.server}/user/reset/password/verify?token=${token}`,
    );
  }

  public resetPasswordConfirm(request: DoResetPasswordRequest): Observable<ApiResponse<null>> {
    return this.http.post<ApiResponse<null>>(`${this.server}/user/reset/password/confirm`, request);
  }

  //#endregion

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
