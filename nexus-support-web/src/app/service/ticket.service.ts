import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { createPaginationOption, Page, Pagination } from '../core/model/request.model';
import { UpdateTicketRequest } from '../core/model/request/update-ticket.request';
import { ApiResponse } from '../core/model/response/api.response';
import { Attachment, Comment, PageTicket, Task, Ticket, TicketDetails, TicketFilters } from '../core/model/ticket.model';
import { mockData } from './mock/mock-data';
import { toHttpParams } from '../shared/utils/request';
import { CreateTaskRequest, UpdateTaskRequest } from '../core/model/request/create-task.request';
import { UpdateCommentRequest } from '../core/model/request/update-comment.request';

@Injectable({
  providedIn: 'root',
})
export class TicketService {
  private http = inject(HttpClient);
  private readonly server: string = environment.API_URL;

  // --- TICKET ---

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

  public updateTicket(request: UpdateTicketRequest): Observable<ApiResponse<TicketDetails>> {
    return this.http.put<ApiResponse<TicketDetails>>(`${this.server}/ticket/update`, request);
  }

  public updateTicketAssignee(assigneePublicId: string, ticketPublicId: string): Observable<ApiResponse<void>> {
    const params = toHttpParams({ assigneePublicId, ticketPublicId });
    return this.http.patch<ApiResponse<void>>(`${this.server}/ticket/update/assignee`, null, { params });
  }

  // --- TASK ---
  public createTask(request: CreateTaskRequest): Observable<ApiResponse<Task>> {
    return this.http.put<ApiResponse<Task>>(`${this.server}/ticket/task`, request);
  }

  public deleteTask(taskPublicId: string): Observable<ApiResponse<void>> {
    const params = toHttpParams({ taskPublicId });
    return this.http.delete<ApiResponse<void>>(`${this.server}/ticket/task/delete`, { params });
  }

  public updateTask(request: UpdateTaskRequest): Observable<ApiResponse<Task>> {
    return this.http.put<ApiResponse<Task>>(`${this.server}/ticket/task/update`, request);
  }

  // --- COMMENT  ---

  public addComment(ticketPublicId: string, comment: string): Observable<ApiResponse<Comment>> {
    return this.http.post<ApiResponse<Comment>>(`${this.server}/ticket/comment`, { ticketPublicId, comment });
  }

  public editComment(request: UpdateCommentRequest): Observable<ApiResponse<Comment>> {
    return this.http.patch<ApiResponse<Comment>>(`${this.server}/ticket/comment/update`, request);
  }

  public deleteComment(commentPublicId: string): Observable<ApiResponse<void>> {
    const params = toHttpParams({ commentPublicId });
    return this.http.delete<ApiResponse<void>>(`${this.server}/ticket/comment/delete`, { params });
  }

  // --- FILES ---
  public deleteFile(filePublicId: string): Observable<ApiResponse<void>> {
    const params = toHttpParams({ filePublicId });
    return this.http.delete<ApiResponse<void>>(`${this.server}/ticket/file/delete`, { params });
  }

  public uploadFiles(request: FormData): Observable<ApiResponse<Array<Attachment>>> {
    return this.http.post<ApiResponse<Array<Attachment>>>(`${this.server}/ticket/file/upload`, request);
  }

  public downloadFile(filePublicId: string) {
    return this.http.get(`${this.server}/ticket/file/download/${filePublicId}`, { responseType: 'blob' });
  }

  public getDashboardData(): any {
    return mockData;
  }
}
