package mc.CushyPro.HanaBot;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class LoggingClassLoader extends URLClassLoader {

	final List<ChildLogger> childLochildLoggersders = new ArrayList<>();

	public LoggingClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
		for (URL url : urls) {
			childLochildLoggersders.add(new ChildLogger(url, this));
		}
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		for (ChildLogger childLoader : childLochildLoggersders) {
			try {
				return childLoader.loadClass(name);
			} catch (ClassNotFoundException ignore) {
			}
		}
		throw new ClassNotFoundException(name);
	}

	private static class ChildLogger extends URLClassLoader {
		private final URL url;

		private ChildLogger(URL url, ClassLoader parent) {
			super(new URL[] { url }, parent);
			this.url = url;
		}

		@Override
		protected Class<?> findClass(String name) throws ClassNotFoundException {
			Class<?> aClass = super.findClass(name);
			System.out.println("Loaded from :" + url + " class: " + name);
			return aClass;
		}
	}

}