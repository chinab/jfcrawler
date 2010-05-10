package org.thuir.forum.classifier;

import org.thuir.forum.data.ForumUrl;
import org.thuir.forum.template.Tag;
import org.thuir.forum.template.Template;
import org.thuir.forum.template.TemplateRepository;
import org.thuir.forum.template.Vertex;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.classifier.Classifier;

public class ForumUrlClassifier extends Classifier {
	TemplateRepository lib = TemplateRepository.getInstance();

	@Override
	public void process(Url url) {
		if(!(url instanceof ForumUrl))
			return;

		ForumUrl u = (ForumUrl) url;
		Template tmpl = lib.getTemplate(url.getHost());
		Vertex v = null;
		if(tmpl != null || u.getTag() == null || u.getTag() == Tag.UNKNOWN)
			if((v=tmpl.predict(url)) != null)
				u.setTag(v.getTag());

	}

}
