/*
 * This file is part of the Emulation-as-a-Service framework.
 *
 * The Emulation-as-a-Service framework is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * The Emulation-as-a-Service framework is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the Emulation-as-a-Software framework.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package de.bwl.bwfla.emucomp.common.database.document;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.json.JsonWriterSettings;
import org.mongojack.JacksonMongoCollection;
import org.mongojack.internal.MongoJackModule;

import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class DocumentDatabase
{
	private final MongoDatabase db;

	DocumentDatabase(MongoDatabase db)
	{
		this.db = db;
	}

	/** Get or create a document-collection */
	public <T> DocumentCollection<T> collection(String cname, Class<T> clazz)
	{
		// Configure object-mapper to use MongoJack module (with JAX-B support)
		final ObjectMapper mapper = MongoJackModule.configure(new ObjectMapper())
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.registerModule(new JaxbAnnotationModule());

		final MongoCollection<T> collection = JacksonMongoCollection.builder()
				.withObjectMapper(mapper)
				.build(db, cname, clazz, UuidRepresentation.STANDARD);

		return new DocumentCollection<T>(collection);
	}

	/** List all collections */
	public Stream<String> collections()
	{
		return StreamSupport.stream(db.listCollectionNames().spliterator(), false);
	}

	/** Drop this database */
	public void drop()
	{
		db.drop();
	}

	/** Validate all collections of this database */
	public void validate()
	{
		int numCollections = 0;

		final var settings = JsonWriterSettings.builder()
				.indent(true)
				.indentCharacters("    ")
				.build();

		final var log = Logger.getLogger("DOCUMENT-DB");
		log.info("Validating database '" + db.getName() + "'...");
		for (final var cname : db.listCollectionNames()) {
			final var command = new Document()
					.append("validate", cname)
					.append("full", true);

			final var result = db.runCommand(command);
			log.info("Validation results for collection '" + cname + "':\n" + result.toJson(settings));
			if (!result.getBoolean("valid"))
				throw new IllegalStateException("Collection '" + cname + "' is invalid!");

			++numCollections;
		}

		log.info(numCollections + " collection(s) validated");
	}
}
