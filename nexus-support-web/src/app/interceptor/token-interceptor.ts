import { HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { shouldNotIntercept } from './skip-url-helper';
import { StorageService } from '../service/storage.service';
import { inject } from '@angular/core';
import { Key } from '../enum/cache.key';

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  const storage = inject(StorageService);
  if (shouldNotIntercept(req.url)) return next(req);
  else {
    return next(requestWithToken(req, storage.get(Key.TOKEN)));
  }
};

const requestWithToken = (request: HttpRequest<unknown>, accesToken: string): HttpRequest<any> => {
  return request.clone({ setHeaders: { Authorization: `Bearer ${accesToken}` } });
};
