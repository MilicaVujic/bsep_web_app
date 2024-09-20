import { Address } from './address.model';
import { User } from './user.model';

export interface Client {
  id: number;
  phone: string;
  pib?: string;
  type?: string;
  address:Address;
  user: User;
}
