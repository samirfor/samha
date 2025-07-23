export interface ErrorResponse {
  timestamp: Date;
  code: number;
  status: string;
  message: string;
  stackTrace: string;
  data: any;
}
