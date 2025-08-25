import { plans } from "./config";
import { Plan } from "./types";

export function getPlanById(planId : string): Plan | undefined{
    return plans.find(plan => plan.id === planId);
}

export function isValidPlanId(planId: string): boolean{
    return plans.some(plan => plan.id === planId);
}