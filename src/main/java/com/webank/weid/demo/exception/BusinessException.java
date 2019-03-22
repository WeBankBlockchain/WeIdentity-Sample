/*
 *       Copyright© (2019) WeBank Co., Ltd.
 *
 *       This file is part of weidentity-sample.
 *
 *       weidentity-sample is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       weidentity-sample is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with weidentity-sample.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.webank.weid.demo.exception;

/**
 * Business Exception.
 * @author v_wbgyang
 *
 */
public class BusinessException extends RuntimeException {

    /**
     * Version UID.
     */
    private static final long serialVersionUID = -3606254083553524719L;

    /**
     * constructor.
     * @param message exception message
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * constructor.
     * @param message exception message
     * @param e  exception object
     */
    public BusinessException(String message, Throwable e) {
        super(message, e);
    }
}
