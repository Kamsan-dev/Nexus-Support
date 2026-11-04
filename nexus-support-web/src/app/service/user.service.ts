import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../core/model/response/api.response';
import { DoResetPasswordRequest } from '../core/model/request/change-password.request';
import { CreateUserRequest } from '../core/model/request/create-user.request';
import { PageUsers, Profile, User } from '../core/model/user.model';
import { createPaginationOption, Page, Pagination } from '../core/model/request.model';
import { PageTicket } from '../core/model/ticket.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private http = inject(HttpClient);
  private readonly server: string = `${environment.API_URL}/${environment.API_VERSION}`;

  public register(request: CreateUserRequest): Observable<ApiResponse<null>> {
    return this.http.post<ApiResponse<null>>(`${this.server}/users/register`, request);
  }

  public verifyAccount(token: string): Observable<ApiResponse<null>> {
    return this.http.post<ApiResponse<null>>(`${this.server}/users/verify/account?token=${token}`, null);
  }

  public getProfile(): Observable<ApiResponse<Profile>> {
    return this.http.get<ApiResponse<Profile>>(`${this.server}/users/profile`);
  }

  //#region password management

  public resetPassword(formData: FormData): Observable<ApiResponse<null>> {
    return this.http.post<ApiResponse<null>>(`${this.server}/users/reset/password`, formData);
  }

  public verifyResetPasswordToken(token: string): Observable<ApiResponse<null>> {
    return this.http.get<ApiResponse<null>>(`${this.server}/users/reset/password/verify?token=${token}`);
  }

  public resetPasswordConfirm(request: DoResetPasswordRequest): Observable<ApiResponse<null>> {
    return this.http.post<ApiResponse<null>>(`${this.server}/users/reset/password/confirm`, request);
  }

  //#endregion

  public getUsers(pageRequest: Pagination): Observable<ApiResponse<Page<PageUsers>>> {
    let params = createPaginationOption(pageRequest);
    return this.http.get<ApiResponse<Page<PageUsers>>>(`${this.server}/users/get-all`, { params });
  }
}
