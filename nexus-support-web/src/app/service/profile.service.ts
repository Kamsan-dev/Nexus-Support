import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiResponse } from '../core/model/response/api.response';
import { Profile, User } from '../core/model/user.model';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { UpdateProfileRequest } from '../core/model/request/update-profile.request';
import { UpdatePasswordRequest } from '../core/model/request/update-password.request';

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  private http = inject(HttpClient);
  private readonly server: string = `${environment.API_URL}/${environment.API_VERSION}`;

  public updatePasswordUser(request: UpdatePasswordRequest): Observable<ApiResponse<void>> {
    return this.http.patch<ApiResponse<void>>(`${this.server}/users/update/password`, request);
  }

  public toggleAccountEnabled(userPublicId: string): Observable<ApiResponse<User>> {
    return this.http.patch<ApiResponse<User>>(`${this.server}/users/${userPublicId}/account/enabled`, {});
  }
  public toggleAccountExpired(userPublicId: string): Observable<ApiResponse<User>> {
    return this.http.patch<ApiResponse<User>>(`${this.server}/users/${userPublicId}/account/expired`, {});
  }
  public toggleAccountLocked(userPublicId: string): Observable<ApiResponse<User>> {
    return this.http.patch<ApiResponse<User>>(`${this.server}/users/${userPublicId}/account/locked`, {});
  }

  public updateRoleUser(userPublicId: string, roleName: string): Observable<ApiResponse<User>> {
    return this.http.patch<ApiResponse<User>>(`${this.server}/users/${userPublicId}/role`, roleName);
  }

  public enableMfa(): Observable<ApiResponse<User>> {
    return this.http.patch<ApiResponse<User>>(`${this.server}/users/mfa/enable`, {});
  }
  public disableMfa(): Observable<ApiResponse<User>> {
    return this.http.patch<ApiResponse<User>>(`${this.server}/users/mfa/disable`, {});
  }
}
