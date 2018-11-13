package persistence;

import persistence.Bundle;

/**
 * A NoPersistence is a PersistenceMechanism that ignores all method calls.
 * It exists so that code that works with PersistenceMechanisms can avoid NPEs without if-guards.
 *
 * @author Mauricio Aniche
 */
public class NoPersistence implements PersistenceMechanism {

	@Override
	public void write(Bundle bundle) {

	}

	@Override
	public void close() {

	}

}
