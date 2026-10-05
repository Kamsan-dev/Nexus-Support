export interface AuthSession {
  accessToken: string;
  refreshToken?: string;
  expiresAt: number;
}
