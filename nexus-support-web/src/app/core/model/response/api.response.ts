export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}

export interface ApiErrorResponse {
  time: string;
  code: number;
  path: string;
  status: string;
  message: string;
  exception: string;
  data: Record<string, unknown>;
}
