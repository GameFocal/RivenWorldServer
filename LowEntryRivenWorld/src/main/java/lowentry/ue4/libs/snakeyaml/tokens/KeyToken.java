/**
 * Copyright (c) 2008, http://www.snakeyaml.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lowentry.ue4.libs.snakeyaml.tokens;

import lowentry.ue4.libs.snakeyaml.error.Mark;

@SuppressWarnings("all")
public final class KeyToken extends Token {

    public KeyToken(Mark startMark, Mark endMark) {
        super(startMark, endMark);
    }

    @Override
    public ID getTokenId() {
        return ID.Key;
    }
}