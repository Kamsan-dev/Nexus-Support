import { RoleEnum } from '../../enum/role';

export interface Message {
  senderPublicId: string;
  senderFirstname: string;
  senderLastname: string;
  senderEmail: string;
  senderImageUrl: string;

  receiverPublicId: string;
  receiverFirstname: string;
  receiverLastname: string;
  receiverEmail: string;
  receiverImageUrl: string;

  conversationId: string;
  messagePublicId: string;
  title: string;
  content: string;
  status: string;

  createdAt: Date;
  updatedAt: Date;
}

export interface ConversationOverviewsResponse {
  conversations: Array<Message>;
  unreadMessagesCount: number;
}

export interface ConversationMessages {
  messages: Array<Message>;
  messageStatusUpdatedCount: number;
  correspondentPublicId: string;
}

export interface Recipient {
  userPublicId: string;
  firstName: string;
  lastName: string;
  imageUrl: string;
  role: RoleEnum;
}
