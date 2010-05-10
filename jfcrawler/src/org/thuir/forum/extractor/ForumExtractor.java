package org.thuir.forum.extractor;

import java.util.ArrayList;
import java.util.List;

import org.thuir.forum.data.ForumUrl;
import org.thuir.forum.template.Template;
import org.thuir.forum.template.TemplateRepository;
import org.thuir.forum.template.Vertex;
import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.extractor.HTMLExtractor;
import org.thuir.jfcrawler.util.stat.Statistic;

/**
 * @author ruKyzhc
 *
 */
public class ForumExtractor extends HTMLExtractor {
	TemplateRepository lib = TemplateRepository.getInstance();

	@Override
	public List<Url> extractUrls(Page page) {
		List<Url> urls = super.extractUrls(page);

		if(!(page.getUrl() instanceof ForumUrl))
			return urls;

		ForumUrl url = (ForumUrl)page.getUrl();

		if(url.getTag() != null) {
			switch(url.getTag()) {
			case CATALOG: {
				Statistic.get("catalog-counter").inc();
			};break;
			case BOARD: {
				Statistic.get("board-counter").inc();
			};break;
			case THREAD: {
				Statistic.get("thread-counter").inc();
			};break;
			default:
				break;
			}
		}

		Template tmpl = lib.getTemplate(url.getHost());

		Vertex vertex = null;
		if(url.getTag()==null)
			vertex = tmpl.predict(url);
		else
			vertex = tmpl.predictByTag(url.getTag(), url);

		if(vertex == null)
			return urls;

		List<Url> ret = new ArrayList<Url>();
		for(Url u : urls) {
			ForumUrl f = (ForumUrl) u;
			f.setInlinkTag(url.getTag());

			if(vertex.match(f)) {
				f.setTag(vertex.getTag());
				ret.add(f);
			}

			for(Vertex v : vertex.getChildren()) {
				if(v.match(f)) {
					f.setTag(v.getTag());
					ret.add(f);
					break;
				}
			}
		}

		return ret;
	}

}


