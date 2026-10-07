export interface CreateUserRequest {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  bio?: string;
  phone?: string;
}
