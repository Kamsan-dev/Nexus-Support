import { formatDate } from '@angular/common';

export function getFormData(formValue: any, files: File[] | null): FormData {
  const formData = new FormData();
  for (const property in formValue) {
    formData.append(property, formValue[property]);
  }
  if (files && files.length > 0) {
    for (let i = 0; i < files.length; i++) {
      formData.append('files', files[i], files[i].name);
    }
  }
  return formData;
}
