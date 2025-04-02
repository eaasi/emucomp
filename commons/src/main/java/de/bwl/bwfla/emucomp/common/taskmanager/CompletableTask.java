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

package de.bwl.bwfla.emucomp.common.taskmanager;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;


public abstract class CompletableTask<R> extends AbstractTask<R>
{
	protected CompletableTask()
	{
		super();
	}

	/** Task handler to be implemented by subclasses. */
	protected abstract CompletableFuture<R> execute() throws Exception;

	@Override
	public final void run()
	{
		final CompletableFuture<R> result = this.getTaskResult();
		try {
			final BiConsumer<R, Throwable> completer = (outcome, error) -> {
				// Finalize task's completion
				this.markTaskAsDone();
				if (error != null)
					result.completeExceptionally(error);
				else result.complete(outcome);
			};

			this.execute()
					.whenComplete(completer);
		}
		catch (Exception error) {
			result.completeExceptionally(error);
		}
	}
}
