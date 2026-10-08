const keyword = ['register', 'reset', 'verify', 'refresh', 'oauth2'];

export const shouldNotIntercept = (url: string): boolean => {
  return keyword.some((key) => url.includes(key));
};
