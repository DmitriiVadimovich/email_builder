package email;

import java.util.*;

public class EmailBuilder {

    public AddressBuilder subject (String subject) {
        return new AddressBuilder(subject);
    }

    class AddressBuilder implements IAddressBuilder {
        private String subject;
        private String from = "default@mail.ru";;
        private Set<String> to = new HashSet<>();

        private AddressBuilder (String subject) {
            this.subject = subject;
        }

        @Override
        public AddressBuilder from(String from) {
            this.from = from;
            return this;
        }

        @Override
        public CopyBuilder to(String... to) {
            this.to.addAll(Arrays.asList(to));
            return new CopyBuilder(this);
        }
    }

    class CopyBuilder implements ICopyBuilder {
        private String subject;
        private String from;
        private Set<String> to;


        public CopyBuilder (AddressBuilder params) {
            this.subject = params.subject;
            this.to = params.to;
            this.from = params.from;
        }

        @Override
        public ICopyBuilder to(String... to) {
            this.to.addAll(Arrays.asList(to));
            return this;
        }

        @Override
        public IContentBuilder copy(String... copy) {
            return new ContentBuilder(this, copy);
        }
    }

    class ContentBuilder implements IContentBuilder {
        private String subject;
        private String from;
        private Set<String> to;
        private Set<String> copy = new HashSet<>();

        private ContentBuilder(CopyBuilder params, String... copy) {
            this.subject = params.subject;
            this.from = params.from;
            this.to = params.to;
            this.copy.addAll(Arrays.asList(copy));
        }

        @Override
        public IContentBuilder copy(String... copy) {
            this.copy.addAll(Arrays.asList(copy));
            return this;
        }

        @Override
        public Content content() {
            return new Content(this);
        }
    }

    class FinalEmailBuilder implements IFinalEmailBuilder {
        private String subject;
        private String from;
        private Set<String> to;
        private Set<String> copy;
        private Content content;

        public FinalEmailBuilder(Content.Builder builder) {
            this.subject = builder.params.subject;
            this.from = builder.params.from;
            this.to = builder.params.to;
            this.copy = builder.params.copy;
            this.content = new Content(builder.body, builder.signature);
        }

        @Override
        public String build() {
            return this.toString();
        }

        @Override
        public String toString() {
            return String.format("Subject: %s\nFrom: %s\nSend to: %s\nCopy to: %s\nText: %s\n%s",
                    subject, from, to, copy, content.body, content.signature);
        }
    }
    public interface IAddressBuilder {
        IAddressBuilder from (String from);
        ICopyBuilder to (String... to);
    }

    public interface ICopyBuilder {
        ICopyBuilder to (String... to);
        IContentBuilder copy (String... copy);
    }

    public interface IContentBuilder {
        IContentBuilder copy (String... copy);
        Content content ();
    }

    public interface IFinalEmailBuilder {
        String build();
    }

    class Content {
        private ContentBuilder params;
        private String body;
        private String signature;

        private Content (String body, String signature) {
            this.body = body;
            this.signature = signature;
        }

        public Builder body(String body) {
            return new Builder(params, body);
        }

        private Content(ContentBuilder params) {
            this.params = params;
        }

        protected class Builder {
            private ContentBuilder params;
            private String body;
            private String signature;

            private Builder(ContentBuilder params, String body) {
                this.params = params;
                this.body = body;
            }

            public IFinalEmailBuilder signature(String sign) {
                this.signature = sign;
                return new FinalEmailBuilder(this);
            }

        }
    }
}
