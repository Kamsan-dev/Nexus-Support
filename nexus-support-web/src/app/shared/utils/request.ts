import { HttpParams } from '@angular/common/http';

export function getFormData(request: any, files: File[] | null): FormData {
  const formData = new FormData();
  for (const property in request) {
    formData.append(property, request[property]);
  }
  if (files && files.length > 0) {
    for (let i = 0; i < files.length; i++) {
      formData.append('files', files[i], files[i].name);
    }
  }
  return formData;
}

export function createFormData(nameRequest: string, request: any, files: File[] | null): FormData {
  const formData = new FormData();
  formData.append(
    nameRequest,
    new Blob([JSON.stringify(request)], {
      type: 'application/json',
    }),
  );
  if (files && files.length > 0) {
    for (let i = 0; i < files.length; i++) {
      formData.append('files', files[i], files[i].name);
    }
  }
  return formData;
}

export function formatFileSize(size: number): string {
  const units: string[] = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
  if (size == 0) return '0 Bytes';
  let quotient = Math.floor(Math.log10(size) / 3);
  quotient = quotient < units.length ? quotient : units.length - 1;
  size = size / 1000 ** quotient;
  return `${+size.toFixed(2)} ${units[quotient]}`;
}

export function toHttpParams<T extends object>(params: T): HttpParams {
  return Object.entries(params).reduce((httpParams, [key, value]) => {
    if (value !== null && value !== undefined) {
      return httpParams.set(key, String(value));
    }

    return httpParams;
  }, new HttpParams());
}
