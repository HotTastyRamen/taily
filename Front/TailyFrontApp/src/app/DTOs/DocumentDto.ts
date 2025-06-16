export interface DocumentDto {
  id: number;
  title: string;
  status: string;
  createdAt: string; // либо Date, либо Instant как string
  lastVersionPath: string;
  isSignedByUser: boolean;
}
