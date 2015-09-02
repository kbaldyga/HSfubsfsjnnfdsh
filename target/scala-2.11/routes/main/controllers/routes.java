
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/kamil/github/hireable/conf/routes
// @DATE:Wed Sep 02 16:03:46 CEST 2015

package controllers;

import router.RoutesPrefix;

public class routes {
  
  public static final controllers.ReverseContractor Contractor = new controllers.ReverseContractor(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseAssets Assets = new controllers.ReverseAssets(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseTrade Trade = new controllers.ReverseTrade(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseApplication Application = new controllers.ReverseApplication(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final controllers.javascript.ReverseContractor Contractor = new controllers.javascript.ReverseContractor(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseAssets Assets = new controllers.javascript.ReverseAssets(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseTrade Trade = new controllers.javascript.ReverseTrade(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseApplication Application = new controllers.javascript.ReverseApplication(RoutesPrefix.byNamePrefix());
  }

}
