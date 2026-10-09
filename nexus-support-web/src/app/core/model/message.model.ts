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
