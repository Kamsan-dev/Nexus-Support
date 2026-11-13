import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiResponse } from '../core/model/response/api.response';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { ConversationMessages, ConversationOverviewsResponse, Message, Recipient } from '../core/model/message.model';
import { NewMessageRequest } from '../core/model/request/new-message.request';

@Injectable({
  providedIn: 'root',
})
export class MessageService {
  private http = inject(HttpClient);
  private readonly server: string = `${environment.API_URL}/${environment.API_VERSION}`;

  public getConversations(): Observable<ApiResponse<ConversationOverviewsResponse>> {
    return this.http.get<ApiResponse<ConversationOverviewsResponse>>(`${this.server}/notifications/message/get-all`);
  }

  public getRecipients(): Observable<ApiResponse<Array<Recipient>>> {
    return this.http.get<ApiResponse<Array<Recipient>>>(`${this.server}/notifications/message/recipients`);
  }

  public getConversationMessages(conversationId: string): Observable<ApiResponse<ConversationMessages>> {
    return this.http.get<ApiResponse<ConversationMessages>>(`${this.server}/notifications/message/${conversationId}`);
  }

  public sendMessage(payload: NewMessageRequest): Observable<ApiResponse<Message>> {
    return this.http.post<ApiResponse<Message>>(`${this.server}/notifications/message`, payload);
  }

  public sendMessageInConversation(payload: NewMessageRequest, conversationId: string): Observable<ApiResponse<Message>> {
    return this.http.post<ApiResponse<Message>>(`${this.server}/notifications/message/${conversationId}`, payload);
  }
}
