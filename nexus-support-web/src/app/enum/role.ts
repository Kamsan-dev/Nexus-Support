export enum RoleEnum {
  USER = 'USER',
  TECH_SUPPORT = 'TECH_SUPPORT',
  MANAGER = 'MANAGER',
  ADMIN = 'ADMIN',
  SUPER_ADMIN = 'SUPER_ADMIN',
}

export const RoleLabel: Record<RoleEnum, string> = {
  [RoleEnum.USER]: 'User',
  [RoleEnum.TECH_SUPPORT]: 'Tech Support',
  [RoleEnum.MANAGER]: 'Manager',
  [RoleEnum.ADMIN]: 'Admin',
  [RoleEnum.SUPER_ADMIN]: 'Super Admin',
};
