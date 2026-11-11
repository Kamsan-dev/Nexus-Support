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

  messageId: number | null;
  messagePublicId: string;
  subject: string;
  content: string;
  status: string;

  createdAt: string;
  updatedAt: string;
}

export interface ConversationOverview {
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
  lastMessageContent: string;
  lastMessageStatus: string;

  createdAt: Date;
  updatedAt: Date;
}

export interface ConversationOverviewsResponse {
  conversations: Array<ConversationOverview>;
  unreadMessagesCount: number;
}

export interface ConversationMessages {
  messages: Array<Message>;
  messageStatusUpdatedCount: number;
  correspondentPublicId: string;
}
