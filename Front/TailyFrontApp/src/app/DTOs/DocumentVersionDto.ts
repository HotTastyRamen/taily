export interface DocumentVersionDto {
  id: number;
  versionNumber: number;
  uploadedAt: string;
  filePath: string;
  documentId: number;
  downloadUrl?: string; // можно вычислять вручную
}
