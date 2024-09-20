import { Address } from './address.model';

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  username: string;
  password: string;
  address: Address;
  phone: string;
  pib?: string;
  packageType?: string;
  type?: string;
  email: string;
  requestStatus?: string;
  blocked:boolean
}
