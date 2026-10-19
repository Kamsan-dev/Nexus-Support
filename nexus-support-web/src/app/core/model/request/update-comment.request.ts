export interface UpdateCommentRequest {
  ownerCommentPublicId: string;
  ticketPublicId: string;
  commentPublicId: string;
  comment: string;
}
