import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiResponse } from '../core/model/response/api.response';
import { Profile, User } from '../core/model/user.model';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { UpdateProfileRequest } from '../core/model/request/update-profile.request';

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  private http = inject(HttpClient);
  private readonly server: string = `${environment.API_URL}/${environment.API_VERSION}`;

  public updateUserProfile(request: UpdateProfileRequest): Observable<ApiResponse<User>> {
    return this.http.put<ApiResponse<User>>(`${this.server}/users/update`, request);
  }
}
