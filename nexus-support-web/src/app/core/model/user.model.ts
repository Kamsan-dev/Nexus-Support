import { Role } from '../../enum/role';

export interface User {
  userPublicId: string;
  email: string;
  firstName: string;
  lastName: string;
  memberId: string;
  bio?: string;
  imageUrl: string;
  phone?: string;
  address?: string;
  qrCodeImageUri: string;
  isUsingMfa: boolean;
  lastLogin: string;
  createdAt: string;
  updatedAt: string;
  role: Role;
  authorities: string;
  isAccountExpired: boolean;
  isAccountLocked: boolean;
  isAccountEnabled: boolean;
}
