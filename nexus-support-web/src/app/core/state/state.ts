import { User } from '../model/user.model';

export interface State {
  loading: boolean;
  profile?: User;
  user?: User;
  ticketDetail?: any;
  tickets?: any[];
  allTickets?: any[];
  pages?: number;
  currentPage?: number;
  reportRequest?: {};
  error?: string;
  query?: any;
  users?: User[];
  report?: any[];
  messages: any[];
  conversation?: any[];
  devices?: any[];
}

export const initialState: State = {
  loading: false,
  messages: [],
};
