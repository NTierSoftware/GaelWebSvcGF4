package com.ntier.rest.model;

//import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement
public class Deployment {

public final Module mModule;
public final Deploy mDeployment;	
	
public final String Versionstring,
	  		  		Versionnum,
	  		  		XtraInfo;

public final int Deploynum;  //eg. 2 for DEV2 or 1 for TEST1
public Deployment(Module module,
				  Deploy deployment,
				  int deploynum, 
				  String versionstring,
				  String versionnum,
				  String xtraInfo) {

	mDeployment = deployment;
	mModule = module;
	Deploynum = deploynum;
	Versionstring = versionstring;
	Versionnum = versionnum;
	XtraInfo = xtraInfo;
}//cstr

public Deployment(Module module,
		  Deploy deployment,
		  String versionstring,
		  String versionnum
		  ) {
	this(module, deployment, 0, versionstring, versionnum, "");
}//cstr

public Deployment(String module,
		  String deployment,
		  String versionstring,
		  String versionnum
		  ) {
	this(Module.valueOf(module), Deploy.valueOf(deployment), 0, versionstring, versionnum, "");
}//cstr


//no-arg constructor required for webservice marshal/unmarshal serialization
// TODO test private no-arg cstr private Location(){} AND  private members too.
public static int Unknown = -9;
public Deployment(){
	this(Module.UNK, Deploy.UNK, Unknown, "","","");
}//cstr
//{"mModule":"UNKNOWN","mDeployment":"UNKNOWN","Versionstring":"","Versionnum":"","XtraInfo":"","Deploynum":-9}


@Override
public String toString(){
	return new StringBuilder()
			.append(mModule.name())
			.append(":")
			.append(mDeployment.name())
			.append(Deploynum)
			.append(":")
			.append(Versionstring)
			.append(":")
			.append(Versionnum)
			.append( (XtraInfo.isEmpty()? "": ": " + XtraInfo) )
	.toString();
}


public enum Module {
	UNK,
	APP	{
		@Override
		public boolean isUP(){return false;}
	},
		
	GPS, 
	NET,
	WEB,
	DB
	;
	
	@SuppressWarnings("static-method")
	public boolean isUP(){ return false; }
 
}//Module

public enum Deploy {
	UNK,
	DEV,
	DVS, //Shared Dev
	TST,
	STG,
	PRD
	;
}//Deploy

public boolean test(){
	return (mDeployment != Deploy.UNK);
}//test


}//Deployment
