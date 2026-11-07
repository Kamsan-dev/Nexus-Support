import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { createPaginationOption, Page, Pagination } from '../core/model/request.model';
import { CreateTaskRequest, UpdateTaskRequest } from '../core/model/request/create-task.request';
import { TicketReportRequest } from '../core/model/request/report-request';
import { UpdateCommentRequest } from '../core/model/request/update-comment.request';
import { UpdateTicketRequest } from '../core/model/request/update-ticket.request';
import { ApiResponse } from '../core/model/response/api.response';
import { TicketDashboardStats } from '../core/model/response/dashboard.model';
import { Attachment, Comment, PageTicket, Task, Ticket, TicketDetails, TicketFilters, TicketReport } from '../core/model/ticket.model';
import { toHttpParams } from '../shared/utils/request';

@Injectable({
  providedIn: 'root',
})
export class TicketService {
  private http = inject(HttpClient);
  private readonly server: string = `${environment.API_URL}/${environment.API_VERSION}`;

  // --- TICKET ---

  public getTickets(pageRequest: Pagination, filters: TicketFilters = {}): Observable<ApiResponse<Page<PageTicket>>> {
    let params = createPaginationOption(pageRequest, filters);
    return this.http.get<ApiResponse<Page<PageTicket>>>(`${this.server}/tickets/list`, { params });
  }

  public createTicket(formData: FormData): Observable<ApiResponse<PageTicket>> {
    return this.http.post<ApiResponse<PageTicket>>(`${this.server}/tickets/create`, formData);
  }

  public getTicket(ticketsPublicId: string): Observable<ApiResponse<Ticket>> {
    return this.http.get<ApiResponse<Ticket>>(`${this.server}/tickets/${ticketsPublicId}`);
  }

  public updateTicket(request: UpdateTicketRequest): Observable<ApiResponse<TicketDetails>> {
    return this.http.put<ApiResponse<TicketDetails>>(`${this.server}/tickets/update`, request);
  }

  public updateTicketAssignee(assigneePublicId: string, ticketPublicId: string): Observable<ApiResponse<void>> {
    const params = toHttpParams({ assigneePublicId, ticketPublicId });
    return this.http.patch<ApiResponse<void>>(`${this.server}/tickets/update/assignee`, null, { params });
  }

  // --- TASK ---
  public createTask(request: CreateTaskRequest): Observable<ApiResponse<Task>> {
    return this.http.post<ApiResponse<Task>>(`${this.server}/tickets/task`, request);
  }

  public deleteTask(taskPublicId: string): Observable<ApiResponse<void>> {
    const params = toHttpParams({ taskPublicId });
    return this.http.delete<ApiResponse<void>>(`${this.server}/tickets/task/delete`, { params });
  }

  public updateTask(request: UpdateTaskRequest): Observable<ApiResponse<Task>> {
    return this.http.put<ApiResponse<Task>>(`${this.server}/tickets/task/update`, request);
  }

  // --- COMMENT  ---

  public addComment(ticketPublicId: string, comment: string): Observable<ApiResponse<Comment>> {
    return this.http.post<ApiResponse<Comment>>(`${this.server}/tickets/comment`, { ticketPublicId, comment });
  }

  public editComment(request: UpdateCommentRequest): Observable<ApiResponse<Comment>> {
    return this.http.patch<ApiResponse<Comment>>(`${this.server}/tickets/comment/update`, request);
  }

  public deleteComment(commentPublicId: string): Observable<ApiResponse<void>> {
    const params = toHttpParams({ commentPublicId });
    return this.http.delete<ApiResponse<void>>(`${this.server}/tickets/comment/delete`, { params });
  }

  // --- FILES ---
  public deleteFile(filePublicId: string): Observable<ApiResponse<void>> {
    const params = toHttpParams({ filePublicId });
    return this.http.delete<ApiResponse<void>>(`${this.server}/tickets/file/delete`, { params });
  }

  public uploadFiles(request: FormData): Observable<ApiResponse<Array<Attachment>>> {
    return this.http.post<ApiResponse<Array<Attachment>>>(`${this.server}/tickets/file/upload`, request);
  }

  public downloadFile(filePublicId: string) {
    return this.http.get(`${this.server}/tickets/file/download/${filePublicId}`, { responseType: 'blob' });
  }

  // --- DASHBOARD ---

  public getDashboardStats(): Observable<ApiResponse<TicketDashboardStats>> {
    return this.http.get<ApiResponse<TicketDashboardStats>>(`${this.server}/tickets/dashboard`);
  }

  // --- REPORT ---

  public getTicketsReport(pageRequest: Pagination, request: TicketReportRequest): Observable<ApiResponse<Page<TicketReport>>> {
    let params = createPaginationOption(pageRequest);
    return this.http.post<ApiResponse<Page<TicketReport>>>(`${this.server}/tickets/report`, request, { params });
  }
}
