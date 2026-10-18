import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { createPaginationOption, Page, Pagination } from '../core/model/request.model';
import { UpdateTicketRequest } from '../core/model/request/update-ticket.request';
import { ApiResponse } from '../core/model/response/api.response';
import { PageTicket, Task, Ticket, TicketFilters } from '../core/model/ticket.model';
import { mockData } from './mock/mock-data';
import { toHttpParams } from '../shared/utils/request';
import { CreateTaskRequest } from '../core/model/request/create-task.request';

@Injectable({
  providedIn: 'root',
})
export class TicketService {
  private http = inject(HttpClient);
  private readonly server: string = environment.API_URL;

  public getTickets(pageRequest: Pagination, filters: TicketFilters = {}): Observable<ApiResponse<Page<PageTicket>>> {
    let params = createPaginationOption(pageRequest, filters);
    return this.http.get<ApiResponse<Page<PageTicket>>>(`${this.server}/ticket/list`, { params });
  }

  public createTicket(formData: FormData): Observable<ApiResponse<PageTicket>> {
    return this.http.post<ApiResponse<PageTicket>>(`${this.server}/ticket/create`, formData);
  }

  public getTicket(ticketPublicId: string): Observable<ApiResponse<Ticket>> {
    return this.http.get<ApiResponse<Ticket>>(`${this.server}/ticket/${ticketPublicId}`);
  }

  public updateTicket(request: UpdateTicketRequest): Observable<ApiResponse<void>> {
    return this.http.put<ApiResponse<void>>(`${this.server}/ticket/update`, request);
  }

  public updateTicketAssignee(assigneePublicId: string, ticketPublicId: string): Observable<ApiResponse<void>> {
    const params = toHttpParams({ assigneePublicId, ticketPublicId });
    return this.http.patch<ApiResponse<void>>(`${this.server}/ticket/update/assignee`, null, { params });
  }

  public createTask(request: CreateTaskRequest): Observable<ApiResponse<Task>> {
    return this.http.put<ApiResponse<Task>>(`${this.server}/ticket/task`, request);
  }

  public getDashboardData(): any {
    return mockData;
  }
}
