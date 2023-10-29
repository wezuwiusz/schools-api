import adapter from '@sveltejs/adapter-static';

export default {
	kit: {
		adapter: adapter({
			// default options are shown. On some platforms
			// these options are set automatically — see below
			pages: '../src/main/resources/app',
			assets: '../src/main/resources/app',
			fallback: undefined,
			precompress: false,
			strict: true
		})
	}
};
