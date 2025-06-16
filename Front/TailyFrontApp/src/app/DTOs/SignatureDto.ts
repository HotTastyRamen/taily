export interface SignatureDto {
  id: number;
  userId: number;
  status: string;
  signedAt: string; // либо Date, либо Instant как string
  documentId: number;
}
