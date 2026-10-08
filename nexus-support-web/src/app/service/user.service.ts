import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../core/model/response/api.response';
import { DoResetPasswordRequest } from '../core/model/request/change-password.request';
import { CreateUserRequest } from '../core/model/request/create-user.request';
import { Profile } from '../core/model/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
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

  public getProfile(): Observable<ApiResponse<Profile>> {
    return this.http.get<ApiResponse<Profile>>(`${this.server}/user/profile`);
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
