export interface AdvertisementRequest {
    id: number;
    description: string;
    clientId: number;
    expirationTime: string;
    activityStart: string;
    activityEnd: string;
    requestStatus: RequestStatus;
  }
  export enum RequestStatus {
    NEW = 'NEW',
    REJECTED = 'REJECTED',
    ACCEPTED = 'ACCEPTED',
  }
  