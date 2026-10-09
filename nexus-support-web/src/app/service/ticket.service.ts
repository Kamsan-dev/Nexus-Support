import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { createPaginationOption, Page, Pagination } from '../core/model/request.model';
import { ApiResponse } from '../core/model/response/api.response';
import { PageTicket, TicketFilters } from '../core/model/ticket.model';
import { mockData } from './mock/mock-data';

@Injectable({
  providedIn: 'root',
})
export class TicketService {
  private http = inject(HttpClient);
  private readonly server: string = environment.API_URL;

  public getTickets(
    pageRequest: Pagination,
    filters: TicketFilters = {},
  ): Observable<ApiResponse<Page<PageTicket>>> {
    let params = createPaginationOption(pageRequest, filters);
    return this.http.get<ApiResponse<Page<PageTicket>>>(`${this.server}/ticket/list`, { params });
  }

  public getDashboardData(): any {
    return mockData;
  }
}
