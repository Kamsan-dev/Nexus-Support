import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { createPaginationOption, Page, Pagination } from '../core/model/request.model';
import { ApiResponse } from '../core/model/response/api.response';
import { PageTicket, Ticket, TicketFilters } from '../core/model/ticket.model';
import { mockData } from './mock/mock-data';
import { CreateTicketRequest } from '../core/model/request/create-ticket.request';
import { getFormData } from '../shared/utils/request';

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

  public createTicket(formData: FormData): Observable<ApiResponse<PageTicket>> {
    return this.http.post<ApiResponse<PageTicket>>(`${this.server}/ticket/create`, formData);
  }

  public getTicket(ticketPublicId: string): Observable<ApiResponse<Ticket>> {
    return this.http.get<ApiResponse<Ticket>>(`${this.server}/ticket/${ticketPublicId}`);
  }

  public getDashboardData(): any {
    return mockData;
  }
}
