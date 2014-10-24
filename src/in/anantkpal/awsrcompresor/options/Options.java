package in.anantkpal.awsrcompresor.options;

public class Options {
	
	private GlobalOptions globalOptions = new GlobalOptions();
	private JSOptions jsOptions = new JSOptions();
	/**
	 * @return the globalOptions
	 */
	public GlobalOptions getGlobalOptions() {
		return globalOptions;
	}
	public Options(GlobalOptions globalOptions, JSOptions jsOptions) {
		super();
		this.globalOptions = globalOptions;
		this.jsOptions = jsOptions;
	}
	/**
	 * @param globalOptions the globalOptions to set
	 */
	public void setGlobalOptions(GlobalOptions globalOptions) {
		this.globalOptions = globalOptions;
	}
	/**
	 * 
	 */
	public Options() {
	}
	/**
	 * @return the jsOptions
	 */
	public JSOptions getJsOptions() {
		return jsOptions;
	}
	/**
	 * @param jsOptions the jsOptions to set
	 */
	public void setJsOptions(JSOptions jsOptions) {
		this.jsOptions = jsOptions;
	}

}
