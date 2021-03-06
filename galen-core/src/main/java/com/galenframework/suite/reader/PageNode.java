/*******************************************************************************
* Copyright 2015 Ivan Shubin http://galenframework.com
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*   http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/
package com.galenframework.suite.reader;

import java.util.LinkedList;
import java.util.List;

import com.galenframework.parser.GalenPageTestReader;
import com.galenframework.parser.SyntaxException;
import com.galenframework.parser.VarsContext;
import com.galenframework.suite.GalenPageAction;
import com.galenframework.suite.GalenPageTest;
import com.galenframework.parser.GalenPageTestReader;
import com.galenframework.parser.SyntaxException;
import com.galenframework.parser.VarsContext;
import com.galenframework.suite.GalenPageAction;
import com.galenframework.suite.GalenPageTest;

public class PageNode extends Node<GalenPageTest> {

    public PageNode(Line line) {
        super(line);
    }

    @Override
    public Node<?> processNewNode(Line line) {
        ActionNode actionNode = new ActionNode(line);
        add(actionNode);
        return actionNode;
    }

    @Override
    public GalenPageTest build(VarsContext context) {
        GalenPageTest pageTest;
        try {
            pageTest = GalenPageTestReader.readFrom(context.process(getArguments()));
        }
        catch (SyntaxException e) {
            e.setLine(getLine());
            throw e;
        }
        
        List<GalenPageAction> actions = new LinkedList<GalenPageAction>();
        pageTest.setActions(actions);
        
        
        for (Node<?> childNode : getChildNodes()) {
            if (childNode instanceof ActionNode) {
                ActionNode actionNode = (ActionNode)childNode;
                actions.add(actionNode.build(context));
            }
        }
        
        return pageTest;
    }


}
