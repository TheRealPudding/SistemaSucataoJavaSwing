/**
 * 
 */
/**
 * 
 */
module SucataoJavaSwing {
	requires java.desktop;
	requires com.google.gson;
	
	opens main.models to com.google.gson;
}