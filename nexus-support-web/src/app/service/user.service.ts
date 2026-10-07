import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { DoResetPasswordRequest } from '../core/request/change-password.request';
import { CreateUserRequest } from '../core/request/create-user.request';
import { ApiResponse } from '../core/response/api.response';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  isAuthenticated() {
    throw new Error('Method not implemented.');
  }

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
}
