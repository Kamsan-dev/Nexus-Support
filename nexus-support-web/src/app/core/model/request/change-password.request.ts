export interface DoResetPasswordRequest {
  token: string;
  password: string;
  confirmPassword: string;
}
